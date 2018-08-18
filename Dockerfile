FROM openjdk:slim

WORKDIR /blz/

ADD bin /blz/bin
ADD Packages /blz/Packages

ENV PATH=$PATH:/blz/bin

ENTRYPOINT ["blz"]
