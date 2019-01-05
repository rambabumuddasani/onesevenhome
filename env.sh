# install CCAvenu jar in maven path, put the ccavenue jar in f drive and excute the  fallowing mvn command.
mvn install:install-file -Dfile="./one7homeWar/files/ccavutil.jar"  -DgroupId=com.ccavenue.security  -DartifactId=ccavenue -Dversion=1.0 -Dpackaging=jar
