#!/bin/sh

#
# Запуск приложения
#

BIN=$(readlink -f "$0")
BINDIR=$(dirname "$BIN")
LIBDIR="$BINDIR"/../lib
CONFDIR="$BINDIR"/../conf
BASEDIR="$BINDIR"/..

JMXHOST=`awk -F= '/^listen.jmx.host/ { print $2 }' "$CONFDIR"/phonex.properties`
JMXPORT=`awk -F= '/^listen.jmx.port/ { print $2 }' "$CONFDIR"/phonex.properties`

if [ -z "$JAVA_HOME" ]
then
        echo "JAVA_HOME is not set"
        exit 1
fi

# Конфигурация памяти.
JAVA_OPTS="${JAVA_OPTS} -Xmx512m -Xms128m"

# Конфигурация JMX для мониторинга.
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=${JMXPORT} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=${JMXHOST}"

# Classpath
CLASSPATH="$LIBDIR/*"

cd "$BASEDIR"
if [ ! -z "$PHONEX_LOG" ]; then
        "$JAVA_HOME"/bin/java $JAVA_OPTS -cp "$CLASSPATH" ru.joxnet.phonex.Main >> ${PHONEX_LOG} 2>&1 &
else
        "$JAVA_HOME"/bin/java $JAVA_OPTS -cp "$CLASSPATH" ru.joxnet.phonex.Main
fi

if [ ! -z "$PHONEX_PID" ]; then
        echo $! > "$PHONEX_PID"
fi
