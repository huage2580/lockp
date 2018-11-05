#!/system/bin/sh
cp /data/data/com.toxic.lockp/server.jar /data/local/tmp/server.jar
chmod 777 /data/local/tmp/server.jar
chown shell /data/local/tmp/server.jar
export CLASSPATH=/data/local/tmp/server.jar
exec app_process /system com.hua.lockp.Server &