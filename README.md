# 기온별 옷차림(2)

### 기존에 하던 프로젝트랑 다른점은 무엇인가요?

- '또 다른 API를 끌어오면 어떨까?' 라는 이야기를 듣고 시도해 보는 프로젝트라서 네이버 쇼핑 API를 끌어올 예정이고 그에 따른 옷이 어떤것이 있는지 보여주는 프로젝트 입니다.
- 이번에는 Activity가 아닌 Fragment를 이용해 BottomNavigationView를 삽입해 화면을 꾸몄습니다. 그렇게 한 이유는 스토어 기능까지 추가하면 하나의 액티비티로 담으면 복잡할 것으로 
  예상이되서 BottomNavigationView를 이용하기로 했습니다.

### 방금 네이버 쇼핑 API를 끌어온다고 하는데...

- 맞습니다. 뭔가 더 추가할 수 있는 기능이 있지 않을까 고민을 했습니다. 이 API로 현재 날씨에 맞는 옷들이 무엇이 있는지 화면에 적용 시킵니다.
- 옷들을 보여주는 목록을 클릭하면 네이버 페이지로 이동하게 할 예정입니다.
- 첫 화면은 네이버에서 호출 방법을 명시한 방법대로 하고 더 보기 버튼을 누르고 보여주는 정보는 Retrofit을 이용해 호출했습니다.
- 대략적인 이미지

![20210407_234220](https://user-images.githubusercontent.com/68115246/117122084-70ab2800-add0-11eb-8983-b29a20657a06.jpg)

### 위에 그림대로 구현을 어떻게 했나요?
1. 중첩 리싸이클러 뷰 : 위의 이미지 대로 현재 기온에 대한 옷 종류와 그에 따른 리싸이클러 뷰를 보여줘야 합니다. 바깥쪽에는 수직으로 스크롤이 가능한 리싸이클러 뷰를 안에는 수평으로 해당 옷에대한 정보 10개를 보여주는 리사이클러 뷰를 만들었습니다. 

살짝 자세히 설명하자면 바깥쪽 RecyclerView에 적용시킨 Fragment1OutViewHolder 클래스에서 작은 RecyclerView 어댑터를 적용 시키고, 레이아웃을 설정했습니다.

- 중첩된 리사이클러 뷰를 만드는데 도움이 된 블로그 : https://lktprogrammer.tistory.com/176

- 자세한 설명과 결과 : https://www.notion.so/79ee2c52f3a14152886b04055fec9472

2. GridLayout을 기반으로 한 RecyclerView : 스토어 프래그먼트에서 더보기 버튼을 클릭하면 해당 옷에대한 정보를 30개 보여주는 액티비티로 이동합니다.

기존에 RecyclerView를 만드는데 마지막 코드를 GridLayout으로 변경합니다.

- 자세한 설명 : https://github.com/yonggun1996/WeatherCloset-v2-/wiki/GridLayout-X-RecyclerView

3. 웹 뷰 이동 : 프래그먼트에서 이미지를 클릭하거나 더 보기 버튼을 클릭한 후 이미지를 클릭을 하면 JSON으로 얻어온 링크로 이동하는 소스코드를 작성했습니다.

### 이번엔 어떻게 끌어올건가요?

- 날씨 API는 OKHTTP로 끌어올 예정입니다.
- 또한 네이버 에서는 라이브러리를 사용해도 되지만 데이터를 파싱하는 코드를 게시해놨습니다.
- 출처 : https://developers.naver.com/docs/serviceapi/search/blog/blog.md#%EB%B8%94%EB%A1%9C%EA%B7%B8
- 데이터를 더 확인했을 땐 Retorfit을 이용해서 데이터를 파싱했습니다.
- 또한 GSON 라이브러리를 사용해 JSON 파일을 더 쉽게 코드로 옮길 수 있었습니다.
- 출처 : https://www.youtube.com/watch?v=VjB9zjcWXFs&t=181s
- 네이버 API를 끌어오는데 도움이 된 영상 : https://www.youtube.com/watch?v=C8-SII6S4Bc&t=2663s

### BottomNavigationView를 사용했네요?

- 위에서도 이야기 했듯이 스토어 기능을 넣을 건데 액티비티로 구현하기엔 무겁기도 하고, 화면구성이 복잡할것 같아서 BottomnavigationView를 사용했습니다.
- BottomNavigationView를 알아내고 적용해본 블로그 : https://imleaf.tistory.com/78
- 유튜브 : https://www.youtube.com/watch?v=R1yeoFk-quU

### 어려운 점은 없었나요?

- 우선 프래그먼트에 대해서 알아야 했습니다. 제가 정리했을 때 액티비티는 말 그대로 화면이고, 프래그먼트는 하나의 액티비티 안에서 보여지는 조각을 말합니다. 
- 이를 알게된 블로그 : https://developer88.tistory.com/69
- 또한 프래그먼트 내 버튼을 눌렀을 때 다른 프래그먼트로 이동해야 했습니다. 이 부분을 몰라서 해멨습니다.
- 그리고 네비게이션 바에 있는 버튼을 계속 눌렀을 때 이미지가 제대로 로드가 안됐습니다. NullPoineException을 일으켰습니다.
  
### 이에 대해 어떻게 해결했는지 간단하게 알 수 있을까요?

- 저는 UI에 대한 이벤트를 onActivityCreated() 메소드에 코딩을 했으며, 프래그먼트를 관리하는 BottomnavMain.kt에서관리를 하게끔 했습니다. 
  BottomnavMain.kt에 viewConfilmWeather()를 선언하고 인자값으로 ArrayList를 받아옵니다. 이를 BottomnavFragment3_1에 넘기는 작업을 코딩했습니다.
  도움이 된 블로그 : https://hijjang2.tistory.com/255?category=856483
  
- BottomMain에서 API를 끌어들어와 네비게이션 바를 누르면 해당하는 프래그먼트에 데이터를 전달해 속도를 빠르게 했습니다.

- 또한 코틀린 코드에서 모든 레이아웃에 null을 허용하는 키워드(?.)를 달아 빠르게 화면을 전환해도 에러가 생기지 않았습니다.

- 위치 권한을 수락했다면 splash 화면에서 main화면으로 넘기게 했고, 데이터를 받아들이기 전까지 프로그레스바를 활성화시켜 대기하게 합니다. 만약 로딩 중 네비게이션 바를 누른다면 다른 화면으로 넘어가지 못하게 설정했습니다. 이러한 방법이 위에서 이야기한 이미지가 로드 되기도 전에 다른 네비게이션 버튼을 누르면 에러를 발생시키는 것을 어느정도 해결시켰습니다.
  
- 또한 설정한 시간의 날씨를 보여주는 액티비티에서 뒤로가기를 눌렀을 때 시간을 설정하는 액티비티로 향하게 했습니다.

### 코루틴 사용기

- 네이버 API를 끌어오던 중 UI작업을 하는 공간 따로, API를 끌어오는 공간이 따로 있어야 하는걸 알게 되었습니다. 그 이유는 그렇게 하지 않으면 networkonmainthreadexception을 일으켰습니다.

- 특히 코틀린에서는 코루틴을 지원하며 Android Developer 사이트에서는 스레드를 이용하는 것 대신 코루틴 사용을 권장했습니다.

- Android Developer 사이트에 설명된 코루틴 : https://developer.android.com/kotlin/coroutines

- 코루틴과 관련된 정리 : https://www.notion.so/Kotlin-Coroutine-131681c56f334feca01c0535064e42ae

### 뷰 바인딩 사용기

- 안드로이드 공식 문서를 살펴보던 중 ViewBinding 기능이 있다는 것을 알게 되었습니다. 평소에 코틀린에선 xml 레이아웃의 아이디를 바로 입력하면 됐지만 파일이 많아질수록 새로운 변수명을
  지어야 한다는 부담감이 있었습니다.
  
- 뷰 바인딩은 레이아웃 id명이 겹쳐도 kotlin 코드에서 해당 뷰에대한 변수를 선언한 후 해당 레이아웃의 아이디에 접근하기 때문에 레이아웃을 선택하는데 모호성이 사라지게 됩니다.

- findviewbyID는 잘못된 타입으로 캐스팅을 할 경우 Class case Exception을 발생시키는 단점과 코드가 길어질 가능성, 레이아웃 태그를 순회해 일치하는 뷰를 찾아가는 방법으로
  연산속도가 떨어지는 단점을 보완합니다.
  
- 알게된 출처 : https://roomedia.tistory.com/entry/View-Binding-findViewById-%EB%8C%80%EC%B2%B4%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95-View-Binding%EA%B3%BC-Data-Binding-%EC%B0%A8%EC%9D%B4
- 정리한 노션 링크 : https://www.notion.so/ViewBinding-vs-findviewByID-9a8d54aaab754f3dbfa3816ffb5c0ecd

### 포트폴리오 PDF 파일
링크 : file:///C:/Users/NOTE%20BOOK/Desktop/%EA%B8%B0%EC%98%A8%EB%B3%84%20%EC%98%B7%EC%B0%A8%EB%A6%BC%20%EC%95%B1(version2).pdf
