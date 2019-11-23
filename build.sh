cd /home/naigo/NetBeansProjects/Restaurant/
rm --recursive ./pack
mkdir pack

mvn clean install -DskipTests
cp ./target/Restaurant*.war ./pack/restaurant-core.war

cd /home/naigo/workspace/naigoapps/code/restaurant-web/
yarn build
cp -R ./WEB-INF ./build/WEB-INF
cd ./build
zip -r restaurant.zip ./*
mv ./restaurant.zip /home/naigo/NetBeansProjects/Restaurant/pack/restaurant-ui.war

cd /home/naigo/AndroidStudioProjects/RestaurantMobile/
./gradlew assembleDebug
cp ./app/build/outputs/apk/debug/*.apk /home/naigo/NetBeansProjects/Restaurant/pack/restaurant-mobile.apk
