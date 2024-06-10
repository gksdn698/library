package com.jafa.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.jafa.domain.MemberDTO;
import com.jafa.domain.MemberVO;

public interface MemberRepository {
	
    @Select("SELECT * FROM tbl_member WHERE memberId = #{memberId}")
    @Results(id = "userInfo", value = {
            @Result(property = "memberId", column = "memberId"),
            @Result(property = "memberPwd", column = "memberPwd"),
            @Result(property = "confirmPwd", column = "confirmPwd"),
            @Result(property = "memberName", column = "memberName"),
            @Result(property = "memberPhone", column = "memberPhone"),
            @Result(property = "memberGender", column = "memberGender"),
            @Result(property = "email", column = "email"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "regDate", column = "regDate"),
            @Result(property = "updateDate", column = "updateDate")
    })
    MemberVO getInfo(String memberId); // 내가 쓴글
    
    @Select("SELECT * FROM tbl_member WHERE memberId = #{memberId}")
    @Results(id = "CommentInfo", value = {
            @Result(property = "memberId", column = "memberId"),
            @Result(property = "memberPwd", column = "memberPwd"),
            @Result(property = "confirmPwd", column = "confirmPwd"),
            @Result(property = "memberName", column = "memberName"),
            @Result(property = "memberPhone", column = "memberPhone"),
            @Result(property = "memberGender", column = "memberGender"),
            @Result(property = "email", column = "email"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "regDate", column = "regDate"),
            @Result(property = "updateDate", column = "updateDate")
    })
    MemberVO CommentInfo(String memberId); // 내가 쓴 댓글
	
	MemberVO read(String memberId); 
	
	MemberDTO readDTO(String memberId);
	
	void insert(MemberVO vo);
	
	void update(MemberVO vo);
	
	void updateDTO(MemberDTO memberDTO);
	
	MemberVO selectById(String memberId);
	
	MemberDTO selectByIdDTO(String memberId);
	
	void updatePassword(
			@Param("memberId") String memberId,  
			@Param("memberPwd") String memberPwd);
	
	String selectByEmail(String email);
	
	 void deleteById(String memberId); // 멤버아이디 삭제
	 
	 void deleteAuthByMemberId(String memberId); // 멤버아이디 권한 부분 삭제
	 
	 void deleteRentByMemberId(String memberId);
	 
	 List<MemberVO> findAll(); // 새로만듬
}