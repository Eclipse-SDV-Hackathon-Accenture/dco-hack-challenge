#!/bin/bash
while ! apt-get update; do
  sleep 1
done
pwd
echo >> ~/.bashrc SUMO_HOME="/sumo"
echo >> ~/.bashrc PATH="$SUMO_HOME/bin:$PATH"
echo starting

apt-get update && apt-get install -y \
  git \
  cmake \
  python3 \
  g++ \
  libxerces-c-dev \
  libfox-1.6-dev \
  libgdal-dev \
  libproj-dev \
  libgl2ps-dev \
  python3-dev \
  python3-pip \
  swig \
  openjdk-17-jdk \
  maven \
  libeigen3-dev

pip3 install matplotlib
pip3 install sumolib
apt-get install python3-lxml
git clone --recursive https://github.com/eclipse-sumo/sumo

mkdir sumo/build && mkdir sumo/build/cmake-build && cd sumo/build/cmake-build
#mkdir $SUMO_HOME/build/cmake-build && cd $SUMO_HOME/build/cmake-build
cmake ../..
make -j$(nproc)

# remove these lines to implement the API
#sumo --version
cd /home
#mvn clean install
java -jar target/*jar
#tail -f /dev/null
