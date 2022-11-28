FROM mcr.microsoft.com/playwright as backend
ENV TZ=Europe/Moscow
ENV LANG=en_US.UTF-8
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-11-jdk maven
USER root
RUN mkdir /carfinesearchbot
COPY / .
RUN mvn install
ENTRYPOINT exec java -cp target/CarFineSearcherBot-1.0.jar -Dloader.path=lib/ org.springframework.boot.loader.PropertiesLauncher

