# BagEasy-back
BagEasy의 백엔드 레포지토리

## 🧳 서비스 설명
<img width="480" alt="스크린샷 2023-07-25 오전 12 53 55" src="https://github.com/EFUB-SURFERS/BagEasy-front/assets/104717341/6861a20e-9762-4b24-99b6-fbb6c598bdbd">

- [x] `BagEasy`는 교환학생을 위한 짐 양도 서비스입니다.
- [x] 기존의 짐 양도 플랫폼들은 여러 곳으로 분산되어 있어 사용자로 하여금 불편함을 초래하는 문제점이 있었습니다.
- [x] 저희 서비스는 이러한 문제점을 해소하기 위해 통합된 형태의 서비스를 제공하고자 했습니다.
- [x] 판매자는 양도하고자 하는 물건을 간편하게 올리고, 구매자는 채팅을 통해 빠르게 판매자와 소통할 수 있도록 개발했습니다.
      
<br>

## 🌟 기능 설명
- [x] 구글 소셜 로그인, 회원가입
- [x] access token / refresh token을 이용한 사용자 인증 기능
- [x] 양도글 작성(이미지 업로드)
- [x] 양도글 수정, 삭제, 조회
- [x] 양도글 찜하기
- [x] 거래 완료 처리
- [x] 양도글별 채팅방 생성
- [x] 일대일 채팅 기능

<br>

## 👥 팀원 소개
|김예지|모수지|박가영|조현영|
|---|---|---|---|
|<img width="300px"  src="https://avatars.githubusercontent.com/u/121334671?v=4">|<img width="300px"  src="https://avatars.githubusercontent.com/u/108855492?v=4">|<img width="300px" src="https://avatars.githubusercontent.com/u/87990290?v=4">|<img width="300px"  src="https://avatars.githubusercontent.com/u/69039161?v=4">
|구글 로그인<br> 닉네임 설정<br> 파견교 설정<br>회원 정보 조회<br> 마이페이지 조회<br> 구매/판매내역 조회<br>토큰 재발급 기능 구현| 양도글 기능 API <br>댓글 기능<br>대댓글(계층형) 기능 API<br>  S3 이미지 업로드 기능 <br>|채팅 기능 API<br>채팅 송수신 기능 <br>Kafka, MongoDB 환경세팅 <br>학교 검색 API 서버 구축 |찜하기<br> 찜 해제하기<br> 찜 여부 조회<br> 찜 목록 조회<br> AWS 환경 세팅 <br>  CI/CD <br> (Blue/Green 무중단 배포)

<br>

## 🔗 기술 스택
- [x] Spring Boot
- [x] Kafka
- [x] MySQL, MongoDB, Redis
- [x] AWS
- [x] Nginx
- [x] Docker
- [x] Github Actions

<br>

## 💥 기술 아키텍처
![](https://velog.velcdn.com/images/goinggoing/post/4d4c4f8d-cca1-4ab3-8a74-6fd114719db1/image.png)

<br>

## 📗 ERD
![](https://velog.velcdn.com/images/goinggoing/post/359d982c-1edb-4d27-b2d3-c407ebfaf5c1/image.png)

<br>

## 📘 MongoDB 스키마
![](https://velog.velcdn.com/images/goinggoing/post/6eeb9332-a958-453d-aeaf-6664605c117f/image.png)

<br>

## 📙 API 명세서
https://documenter.getpostman.com/view/28349851/2s93zE4fsY
