FROM java:8-alpine

MAINTAINER Marco Pensallorto<marco.pensallorto@gmail.com>

ADD target/uberjar/bookmarks.jar /bookmarks/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/bookmarks/app.jar"]
