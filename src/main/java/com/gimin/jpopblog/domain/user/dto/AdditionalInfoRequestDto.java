package com.gimin.jpopblog.domain.user.dto;

import com.gimin.jpopblog.domain.user.entity.Nickname;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AdditionalInfoRequestDto {
    private String nickname;
    // html의 <input type="date">는 "YYYY-MM-DD" 형식의 문자열로 전송됨
    // 클라이언트에서 dto에 값을 채워 json을 보내면 서버는 @RequestBody로 받은 후
    // jackson을 통해 LocalDate.parse("YYYY-MM-DD")를 실행하여 객체로 만든다.
    private LocalDate birthDate;
}
