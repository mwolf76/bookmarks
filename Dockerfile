FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/bookmarks.jar /bookmarks/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/bookmarks/app.jar"]
