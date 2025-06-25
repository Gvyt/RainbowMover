#!/usr/bin/env sh
##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.

DEFAULT_JVM_OPTS=""

APP_NAME="Gradle"

# Use the maximum available, or set MAX_FD != -1 to use that value.

if [ -z "$MAX_FD" ] ; then
  MAX_FD="maximum"
fi

warn() {
  echo "$*"
}

die() {
  echo
  echo "$*"
  echo
  exit 1
}

# OS specific support (must be 'true' or 'false').

cygwin=false
msys=false
darwin=false
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  MINGW*) msys=true ;;
  Darwin*) darwin=true
           ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

GRADLE_HOME=

APP_HOME=`dirname "$0"`
APP_HOME=`cd "$APP_HOME" && pwd`

# Determine the Java command to use to start the JVM.

if [ -n "$JAVA_HOME" ] ; then
  if [ -x "$JAVA_HOME/bin/java" ] ; then
    JAVA="$JAVA_HOME/bin/java"
  else
    die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
  fi
else
  JAVA="java"
fi

exec "$JAVA" $DEFAULT_JVM_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
