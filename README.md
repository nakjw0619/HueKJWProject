# HueKJWProject

고민해야 할 것!! (교수님 알려주세요!)

ㄱ. 전력량 측정 후 데이타 저장 방법

pre-condition
전력량 측정 방법
앱에서 Hue을 켜고 끈 시간을 측정 후 전력소비량을 곱한다. ( Time X 전력소비량 = 전력량)

데이타 저장 방법
방법1. Sqlite를 사용하여 해당 스마트폰에만! 저장을 한다.
 장점 : 앱 개발이 상대적으로 간편해진다.
 단점 : 다른 앱으로 실행한 Hue의 전력량을 측정 할 수 없다.
 
방법2. 원격데이타 저장소를 구현하여 Sqlite에 최신 데이타를 업데이트 시킨다.
 장점 : 가정에서 사용된 Hue의 전력량에 대하여 공유하여, 전체 전력량을 구할 수 있다.
 단점 : 개발이 상대적으로 어렵다.
 
 
ㄴ. 그래프 그리기
 achartengine 오픈 라이브러리를 사용하여 그래프를 그린다.
 그래프는 ㄱ.에서 구한 데이타를 바탕으로 그린다.

