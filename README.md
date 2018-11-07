ToyManager(トイ・マネージャー)  
ToyTalk( https://github.com/hassoubeat/ToyTalk )と対になる予定管理アプリケーション。

ToyManagerはJavaEE7フレームワークを利用して作成されたWEB上で予定管理が行えるWEBアプリケーションです。  
サーバ上に配置して、Googleカレンダーのように予定を登録することが可能です。  

__カレンダー画面__
![screen2](https://user-images.githubusercontent.com/42881127/48115983-fef9ce00-e2a7-11e8-8bba-d6830b0cb1dc.png)
  
__予定登録画面__
![screen](https://user-images.githubusercontent.com/42881127/48115896-b215f780-e2a7-11e8-8060-21898cfc545b.png)
  
  
登録された予定は用意したAPIからToyTalkが取得して、合成音声にて読み上げを行います。
  
__読み上げサンプル__
https://drive.google.com/open?id=0B6QVDoH6tD-fd1ZpUzVrZktHems

追加機能として、読み上げ以外の独自の処理を登録した予定時刻にToyTalkに実行させる"ファセット"という仕組みも実装しています。
FacetInterface( https://github.com/hassoubeat/FacetInterface )を実装させて、ToyManagerから登録することで利用可能になります。

===============
