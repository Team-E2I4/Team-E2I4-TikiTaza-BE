package com.pgms.api.domain.online.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.online.dto.OnlineMemberGetResponse;
import com.pgms.coredomain.repository.GameRoomMemberRepository;
import com.pgms.coredomain.repository.MemberRepository;
import com.pgms.coreinfraredis.entity.OnlineMember;
import com.pgms.coreinfraredis.repository.OnlineMemberRepository;
import com.pgms.coresecurity.resolver.Account;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OnlineService {

	private final MemberRepository memberRepository;
	private final GameRoomMemberRepository gameRoomMemberRepository;
	private final OnlineMemberRepository onlineMemberRepository;

	public Set<OnlineMemberGetResponse> getOnlineMembers(Account account) {
		Set<OnlineMemberGetResponse> onlineMembers = new HashSet<>();

		if (!account.isGuest()) {
			addOnlineMemberForMember(account, onlineMembers);
		} else {
			addOnlineMemberForGuest(account, onlineMembers);
		}
		addGameRoomMembers(onlineMembers);
		addOnlineMembers(onlineMembers);

		return onlineMembers;
	}

	private void addOnlineMemberForMember(Account account, Set<OnlineMemberGetResponse> onlineMembers) {
		memberRepository.findById(account.id())
			.ifPresent(currentMember -> saveAndAddToOnlineMembers(
				account.id(),
				currentMember.getNickname(),
				onlineMembers));
	}

	private void addOnlineMemberForGuest(Account account, Set<OnlineMemberGetResponse> onlineMembers) {
		saveAndAddToOnlineMembers(account.id(), account.nickname(), onlineMembers);
	}

	private void saveAndAddToOnlineMembers(Long memberId, String nickname, Set<OnlineMemberGetResponse> onlineMembers) {
		OnlineMember onlineMember = new OnlineMember(memberId, nickname);
		onlineMemberRepository.save(onlineMember);
		onlineMembers.add(new OnlineMemberGetResponse(onlineMember.getId(), onlineMember.getNickname()));
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
