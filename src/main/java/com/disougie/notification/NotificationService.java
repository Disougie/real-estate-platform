package com.disougie.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.disougie.app_user.AppUser;
import com.disougie.util.PageResponse;
import com.disougie.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
	private final NotificationRepository notificationRepository;
	private final SimpMessagingTemplate messagingTemplate;

	public PageResponse<NotificationResponse> getMyNotifications(int page, int size) {
		 
		Page<NotificationResponse> pageOfNotifications = notificationRepository
				.findAll(PageRequest.of(page, size))
				.map(note -> new NotificationResponse(note.getMessage()));
		
		return new PageResponse<NotificationResponse>(
				 pageOfNotifications.getContent(),
				 pageOfNotifications.getTotalPages(),
				 pageOfNotifications.getTotalElements(),
				 pageOfNotifications.getNumber(),
				 pageOfNotifications.getSize(),
				 pageOfNotifications.isLast()
			);
		
	}
	
	
	//TODO: messaging queue
	
	
	@Async
	public void sendNotification(AppUser recipient, String message) {
		notificationRepository.save(
				new Notification(null,recipient,message,TimeUtil.now())
		);
		messagingTemplate.convertAndSendToUser(
				recipient.getUsername(),
				"/queue/notifications",
				new NotificationResponse(message)
		);
	}
}
