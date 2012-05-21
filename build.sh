## enable, for this terminal session, completion for run under bash
. run.sh --completion--

## create PDF files from dia files
dia Documentation/CNT_class_diagram.dia -e Documentation/CNT_class_diagram.pdf 2>/dev/null
dia Documentation/CNT_join_diagram.dia  -e Documentation/CNT_join_diagram.pdf  2>/dev/null

## rotate PDF files to landscape
pdf270 Documentation/CNT_class_diagram.pdf -o Documentation/CNT_class_diagram.pdf 2>/dev/null
pdf270 Documentation/CNT_join_diagram.pdf  -o Documentation/CNT_join_diagram.pdf  2>/dev/null

## create directory for Java binaries
mkdir bin 2>/dev/null

## copy resources files from source to classpath and working directory
cp src/*.png bin/
cp src/*.png ./

## the Jars used by cnt
jars=":lib/cling-core-1.0.5.jar:lib/cling-support-1.0.5.jar:lib/teleal-common-1.0.13.jar"


## java compilor if default is for Java 7
[[ $(javac -version 2>&1 | cut -d . -f 2) = '7' ]] &&
    function javacSeven()
    {
	javac $@
    }

## java compilor if default is not for Java 7
[[ $(javac -version 2>&1 | cut -d . -f 2) = '7' ]] ||
    function javacSeven()
    {
	javac7 $@
    }


## compile cnt

#javacSeven -Xlint:all -cp .$jars -s src -d bin src/cnt/{*,*/*,*/*/*}.java 2>&1 | less
#javacSeven  -Xlint:all -cp .$jars -s src -d bin src/cnt/{*,{d,g,t,u,m}*/*,*/*/*,n*/{B,O,G,P}*}.java 2>&1 | less
#javacSeven -cp .$jars -s src -d bin src/cnt/{*,{d,g,t,u,m}*/*,*/*/*,n*/{B,O,G,P}*}.java 2>&1 | less

#javacSeven -Xlint:all -cp .$jars -s src -d bin src/cnt/{*,*/*,*/*/*}.java 2>&1
javacSeven  -Xlint:all -cp .$jars -s src -d bin src/cnt/{*,{d,g,t,u,m}*/*,*/*/*,n*/{B,O,G,P,C,T}*}.java 2>&1
#javacSeven -cp .$jars -s src -d bin src/cnt/{*,{d,g,t,u,m}*/*,*/*/*,n*/{B,O,G,P}*}.java 2>&1
