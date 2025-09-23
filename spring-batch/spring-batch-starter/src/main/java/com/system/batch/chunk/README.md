# 청크 지향 처리 (Chunk-Oriented Processing)

## 개념
Spring Batch 의 데이터를 다루는 작업은 **읽기** -> **처리** -> **쓰기** 라는 공통 패턴을 보인다.

Chunk 는 데이터를 일정 단위로 쪼갠 덩어리이며, Spring Batch 에서 데이터 기반 처리 방식을 청크 지향 처리라고 부른다.

백만 건의 데이터를 처리할 때, Spring Batch 는 전체를 한번에 읽고 처리하지 않는다. 대신 100 개씩 쪼개서 읽고 처리하고 저장한다.

이렇게 나뉜 100개의 묶음이 바로 **청크**이다

## 청크 지향 처리

100만 건의 데이터를 한 번에 메모리에 불러오고 처리하고 저장한다면?
- 메모리는 터진다.
- DB는 과부하에 걸린다.
- 전체 시스템이 폭발한다.

1. 메모리를 지켜서 데이터의 양을 줄임
100만 건을 한 번에 메모리에 올리지 않고, DB에 적절한 양의 데이터를 가져온다.
- 100개 씩 나눠서 불러온다.
- 개념적으로, 메모리엔 단 100개의 데이터만 존재한다.

이 방식으로 메모리 사용량은 안정적이고, 시스템은 무리없이 데이터를 처리한다.

2. 가벼운 트랜잭션
트랜잭션은 작업의 성공 또는 실패를 하나의 단위로 묶는 것이다. 하지만 100만 건을 하나의 트랜잭션으로 처리한다면, 작업 중간에 오류가 발생할 경우 100만 건이 전부 롤백된다.
- 청크 단위로 트랜잭션을 나눈다. 따라서 데이터 100개 단위로 처리가 성공하면 커밋하고, 실패하면 롤백한다.

만약 작업 중간에 에러가 발생하는 경우
- 이전 청크는 이미 커밋 완료
- 에러가 발생한 청크만 롤백되고 해당 청크부터 재시작하면 된다.

즉, 복구가 쉽고 빠르다. 청크는 작지만 강력한 복구 시스템이며 작업의 실패를 작은 실패로 제한해준다.

## 청크 지향 처리의 3 작업 - 읽기, 가공, 쓰기

배치 처리는 읽기(ItemReader), 가공(ItemProcessor), 쓰기(ItemWriter) 로 청크 지향 처리가 완료 된다.

### ItemReader - 데이터를 끌어온다

데이터를 읽어오는 것은 배치의 생명줄이다.
```java
public interface ItemReader<T> {
    T read() throws Exception, 
        UnexpectedInputException, 
        ParseException, 
        NonTransientResourceException;
}
```
- read(): 아이템을 하나씩 반환한다(T 타입 리턴) 여기서 아이템이란 파일의 한 줄 또는 데이터베이스의 한 행(row)과 같이 데이터 하나를 의미한다. 
예를 들어, 총 100만 건의 레코드가 있다면, 각각의 레코드를 아이템이라고 부른다. 
ItemReader는 데이터 소스(DB, 파일 등)에서 데이터를 하나씩 순차적으로 읽어온다.
읽을 데이터가 더 이상 없으면 `null`을 반환하며, 스텝은 종료된다.
**ItemReader** 가 `null` 을 반환하는 것이 청크 지향 처리 Step 의 종료 시점이다.
이는 Spring Batch 가 Step 의 완료를 판단하는 핵심 조건이다.

- 다양한 구현체 제공: Spring Batch 는 파일, 데이터베이스, 메시지 큐 등 다양한 데이터 소스에 대한 표준 구현체를 제공한다.
예를 들어, FlatFileItemReader 는 CSV 나 텍스트 파일에서 데이터를 읽어오고, JdbcCursorItemReader 는 관계형 데이터베이스로 부터 데이터를 읽어온다.

### ItemProcessor - 데이터를 가공한다

ItemProcessor 는 데이터를 원하는 형태로 가공한다. ItemReader 가 넘긴 원재료를 받아서 필요한 모양으로 다듬는 작업을 한다.
```java
public interface ItemProcessor<I, O> {
    O process(I item) throws Exception;
}
```
- 데이터 가공: 입력데이터(`I`) 를 원하는 형태(`O`) 로 변환한다. 읽어온 원본 데이터를 비즈니스 로직에 맞게 가공하거나 출력 시스템이 요구하는 형식으로 변환하는 작업이다.
- 필터링: null을 반환하면 해당 데이터는 처리 흐름에서 제외된다. 즉 ItemWriter 로 전달되지 않는다. 유효하지 않은 데이터나 처리할 필요가 없는데이터를 걸러낼 때 사용한다.
- 데이터 검증: 입력 데이터의 유효성을 검사한다. 필터링과 달리 조건에 맞지 않는 데이터를 만나면 예외를 발생시킨다. 필수 필드 누락이나 잘못된 데이터 형식을 발견했을 때 예외를 던져 Batch Job을 중단시킨다.
- 필수 아님: ItemProcessor 는 생략 가능하다. Step 이 데이터를 읽고 바로 쓰도록 구성할 수 있다.

### ItemWriter - 결과물을 기록

ItemWriter 는 ItemProcessor 가 만든 결과물을 받아, 원하는 방식으로 최종 저장/출력한다. - 데이터를 DB 에 `INSERT` / 파일에 `WRITE` / 메시지 큐에 `PUSH` 등
```java
public interface ItemWriter<T> {
    void write(Chunk<? extends T> chunk) throws Exception;
}
```
- 한 덩어리씩 쓴다: ItemWriter 는 데이터를 한 건씩 쓰지 않는다. Chunk 단위로 묶어서 한 번에 데이터를 쓴다. `write()` 메서드의 파라미터 타입이 `chunk` 인 것에 주목해야한다. ItemReader 와 ItemProcessor 가 아이템을 하나씩 반환하고 입력받는 것과 달리, ItemWriter 는 데이터 덩어리를 한 번에 입력받아 한 번에 쓴다.
- 다양한 구현체 제공: Spring Batch 는 파일, 데이터베이스, 외부 시스템 전송 등에 사용할 수 있는 다양한 구현체를 제공한다. FlatFileItemWriter 는 파일에 데이터를 기록하고, JdbcBatchItemWriter 는 데이터베이스에 데이터를 저장한다.

