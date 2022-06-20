# triple-backend
MYSQL에서 Triple.sql 내용을 실행하기.<br>
import의 경우 간혹 가다가, AUTO_INCREMENT 값이 적용 안되는 현상이 있으나, 직접 실행의 경우 문제가 없습니다.
<br>
<br>
application.properties 파일에서 <br>
spring.datasource.url=jdbc:mysql://[아이피]:[포트]/[스키마]?useUnicode=yes&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Seoul <br>
spring.datasource.username=[MYSQL아이디] <br>
spring.datasource.password=[MYSQL비밀번호] <br>
변경하기.

EX)
1. 특정 회원 포인트 조회하기. <br>
http://localhost:8080/points?userId=nipa0711 <br>
/points? 뒤에 쿼리 스트링으로 userId=아이디 입력<br>

2. 특정 회원 포인트 기록 조회하기.<br>
http://localhost:8080/history?userId=nipa0711<br>
/history? 뒤에 쿼리 스트링으로 userId=아이디 입력<br>

3. 리뷰 입력하기.
<pre>
<code>
POST /events
{
    "type": "REVIEW",
    "action": "ADD", /* "MOD", "DELETE" */
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
    "content": "좋아요!",
    "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
</code>
</pre>
<br>
<br>
<img src="https://user-images.githubusercontent.com/3995469/174605165-00d0c577-7045-4ac6-8804-2b13e92a7979.png"/>
![구조](https://user-images.githubusercontent.com/3995469/174605165-00d0c577-7045-4ac6-8804-2b13e92a7979.png)
