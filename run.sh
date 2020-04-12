#!/bin/bash

app=$1
dir='services'
case $app in
configServer) dir='utilServices' ;;
adminServer) dir='utilServices' ;;
discoveryServer) dir='utilServices' ;;
esac

cd $dir/$app/
mvn clean package spring-boot:run