# Kotlin Multiplatform 開発用コンテナ
FROM ubuntu:22.04

ENV TZ=Asia/Tokyo
ENV DEBIAN_FRONTEND=noninteractive

# 必須パッケージをまとめてインストール
RUN apt-get update && \
    apt-get install -y tzdata curl wget unzip zip git openjdk-17-jdk gradle && \
    rm -rf /var/lib/apt/lists/*

# Kotlin CLI インストール
RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install kotlin"

# Android SDK
RUN mkdir -p /opt/android-sdk/cmdline-tools && cd /opt/android-sdk && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip && \
    unzip commandlinetools-linux-11076708_latest.zip -d cmdline-tools && \
    rm commandlinetools-linux-11076708_latest.zip && \
    mv cmdline-tools/cmdline-tools cmdline-tools/latest

ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

RUN yes | sdkmanager --licenses || true
RUN sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

WORKDIR /workspace
CMD ["/bin/bash"]
