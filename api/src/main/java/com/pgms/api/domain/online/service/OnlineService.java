package com.pgms.api.domain.online.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.online.dto.OnlineMemberGetResponse;
import com.pgms.coredomain.repository.GameRoomMemberRepository;
import com.pgms.coreinfraredis.entity.OnlineMember;
import com.pgms.coreinfraredis.repository.OnlineMemberRepository;
import com.pgms.coresecurity.resolver.Account;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OnlineService {

	private final GameRoomMemberRepository gameRoomMemberRepository;
	private final OnlineMemberRepository onlineMemberRepository;

	public Set<OnlineMemberGetResponse> getOnlineMembers(Account account) {
		OnlineMember currentMember = new OnlineMember(account.id(), account.nickname());
		onlineMemberRepository.save(currentMember);
		Set<OnlineMemberGetResponse> onlineMembers = new HashSet<>();
		addGameRoomMembers(onlineMembers);
		addOnlineMembers(onlineMembers);

		return onlineMembers;
	}

	private void addGameRoomMembers(Set<OnlineMemberGetResponse> onlineMembers) {
		gameRoomMemberRepository.findAll().forEach(gameRoomMember ->
			onlineMembers.add(new OnlineMemberGetResponse(
				gameRoomMember.getMemberId(),
				gameRoomMember.getNickname())));
	}

	private void addOnlineMembers(Set<OnlineMemberGetResponse> onlineMembers) {
		onlineMemberRepository.findAll().forEach(onlineMember -> {
			if (onlineMember != null) {
				onlineMembers.add(new OnlineMemberGetResponse(
					onlineMember.getId(),
					onlineMember.getNickname()));
			}
		});
	}
}
