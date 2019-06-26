
# git pull
cd /apps/files/git/intensive-user
git pull

# mvn clean package
cd /apps/files/git/intensive-user/intensive-user
mvn clean package

cp /apps/files/git/intensive-user/intensive-user/target/intensive-user-1.0-SNAPSHOT.jar /apps/files/git/intensive-user/intensive-user/src/main/docker/.
cd /apps/files/git/intensive-user/intensive-user/src/main/docker

#docker build
docker build -t iij57/intensive-user:1.0 -t iij57/intensive-user:latest .

#docker run
docker-compose up -d
