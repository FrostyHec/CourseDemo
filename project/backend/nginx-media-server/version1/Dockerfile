## 使用官方NGINX镜像作为基础镜像
#FROM mugennsou/nginx-http-flv
#
## 将自定义的NGINX配置文件复制到容器中
##COPY nginx.conf /etc/nginx/nginx.conf
#COPY rtmp.conf /etc/nginx/conf.d/rtmp/rtmp.conf
#
## 暴露HTTP和RTMP端口
#EXPOSE 8080 1935
#
## 启动NGINX
#CMD ["nginx", "-g", "daemon off;"]
ARG NGINX_VERSION=1.14.2
ARG NGINX_HTTP_FLV_VERSION=1.2.5

FROM alpine:3.8 AS base

# Add Alpine Linux Chinese mirror
RUN apk add --no-cache pcre openssl


FROM base AS build
ARG NGINX_VERSION
ARG NGINX_HTTP_FLV_VERSION

RUN apk add build-base pcre-dev openssl-dev

WORKDIR /tmp
RUN \
    wget https://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz && \
    tar xzf nginx-${NGINX_VERSION}.tar.gz && \
    wget https://github.com/winshining/nginx-http-flv-module/archive/v${NGINX_HTTP_FLV_VERSION}.tar.gz && \
    tar xzf v${NGINX_HTTP_FLV_VERSION}.tar.gz && \
    cd nginx-${NGINX_VERSION} && \
    ./configure --add-module=../nginx-http-flv-module-${NGINX_HTTP_FLV_VERSION}  \
                --with-http_auth_request_module && \
    make && \
    make install


FROM base AS release
COPY --from=build /usr/local/nginx /usr/local/nginx
COPY nginx.conf /usr/local/nginx/conf/nginx.conf

EXPOSE 1935
EXPOSE 8088

CMD ["/usr/local/nginx/sbin/nginx", "-g", "daemon off;"]