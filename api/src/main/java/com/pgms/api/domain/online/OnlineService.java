package com.pgms.api.domain.online;

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
		HashSet<OnlineMemberGetResponse> onlineMembers = new HashSet<>();

		OnlineMember currentMember = new OnlineMember(account.id(), account.nickname());
		onlineMemberRepository.save(currentMember);

		gameRoomMemberRepository.findAll()
			.forEach(gameRoomMember -> onlineMembers.add(new OnlineMemberGetResponse(
				gameRoomMember.getMemberId(),
				gameRoomMember.getNickname())));

		// onlineMemberRepository.findAll()
		// 	.forEach(onlineMember -> onlineMembers.add(new OnlineMemberGetResponse(
		// 		onlineMember.getId(),
		// 		onlineMember.getNickname())));

		Iterable<OnlineMember> onlineMembersIterable = onlineMemberRepository.findAll();

		// onlineMembersIterable가 null이 아니고 비어 있지 않은 경우에만 forEach 블록 실행
		if (onlineMembersIterable.iterator().hasNext()) {
			onlineMembersIterable.forEach(onlineMember -> onlineMembers.add(new OnlineMemberGetResponse(
				onlineMember.getId(),
				onlineMember.getNickname())));
		}

		return onlineMembers;
	}
}
