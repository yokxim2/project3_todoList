Todo-list

기능목록

* Todo 생성
* Todo 조회
  > 목록 조회,
  > 상세 조회
* Todo 수정
* Todo 삭제

API 정의서

### **API 상세 내용**

Todo 생성

> [POST] /api/todos
> 

request

```json
{
	"title": "제목1",
	"content": "내용1",
	"username": "사용자1",
	"email": "abc@example.com",
	"password": "12345"
}
```

response

<success>

```json
{
	"id": 1,
	"username": "사용자1",
	"title": "제목1",
	"content": "내용1",
	"created_at": "1111-11-11 11:11:11",
	"modified_at": "1111-11-11 11:11:11",
}
```

error

```json
{
	"error": "이미 등록되어 있는 이메일입니다. 알맞은 사용자명 혹은 비밀번호를 입력하십시오."
}
```

예외

1. 이미 Member 테이블에 등록되어 있는 이메일을 입력값으로 넣고, 아이디나 비밀번호가 맞지 않을 경우에는 예외를 반환

---

Todo 전체 조회

> [GET] /api/todos
> 

response

<success>

```json
[
		{
			"id": 1,
			"username": "사용자1",
			"title": "제목1",
			"content": "내용1",
			"created_at": "1111-11-11 11:11:11",
			"modified_at": "1111-11-11 11:11:11",
		},
		{
			"id": 2,
			"username": "사용자2",
			"title": "제목2",
			"content": "내용2",
			"created_at": "1111-11-11 11:11:11",
			"modified_at": "1111-11-11 11:11:11",
		},
		...
]
```

Todo 상세 조회

> [GET] /api/todos?pageNum={pageNum}&pageSize={pageSize}&username={username}&modifiedAt={modifiedAt}
> 

- Page 객체 사용
    1. pageNum
        - 페이지 번호 (Default 1)
    2. pageSize
        - 한 페이지당 데이터 개수 (Default 10)

- username(작성자명), modifiedAt(수정일)로 상세 검색 가능 [패러미터 생략 가능]

response

<success>

```json
{
	"id": 1,
	"username": "사용자1",
	"title": "제목1",
	"content": "내용1",
	"created_at": "1111-11-11 11:11:11",
	"modified_at": "1111-11-11 11:11:11"
}
```

---

Todo 수정

> [PUT] /api/todos/{id}
> 

request

```json
{
	"title": "제목1+",
	"content": "내용1+",
	"password": "1234"
}
```

response

<success>

```json
{
	"id": 1,
	"title": "제목1+",
	"content": "내용1+",
	"created_date": "1111-11-11 11:11:11",
	"last_modified": "2222-22-22 22:22:22"
}
```

error

```json
{
	"error": "일치하는 회원이 없습니다."
}
```

```json
{
	"error": "비밀번호가 올바르지 않습니다."
}
```

예외

1. 일정의 member_id 값과 일치하는 id를 가진 Member 데이터가 없을 경우 예외를 반환
2. 비밀번호가 일치하지 않을 경우 예외를 반환

---

Todo 삭제

> [DELETE] /api/todos/{id}
> 

request

```json
{
	"password": "1234"
}
```

response

<error>

```json
{
	"error": "비밀번호가 올바르지 않습니다."
}
```

에러


1. 비밀번호가 일치하지 않았을 경우 예외 반환

---

ERD


![Project3_erd](https://github.com/user-attachments/assets/380ccb09-332d-4e9f-bfd2-af34bdb1ac3b)


---

트러블슈팅 TIL 링크

https://ultra-comb-ede.notion.site/API-1117b062fda780f2aabff02d3127d76b?pvs=4
