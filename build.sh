mkdir bin 2>/dev/null

. run.sh --completion--

cp src/*.png bin/
cp src/*.png ./

dia Documentation/CNT_class_diagram.dia -e Documentation/CNT_class_diagram.pdf 2>/dev/null

jars=":lib/cling-core-1.0.5.jar:lib/cling-support-1.0.5.jar:lib/teleal-common-1.0.13.jar"


[[ $(javac -version 2>&1 | cut -d . -f 2) = '7' ]] &&
    function javacSeven()
    {
	javac $@
    }


[[ $(javac -version 2>&1 | cut -d . -f 2) = '7' ]] ||
    function javacSeven()
    {
	javac7 $@
    }


#javacSeven -Xlint:all -cp .$jars -s src -d bin src/cnt/{*,*/*,*/*/*}.java 2>&1 | less
#javacSeven  -Xlint:all -cp .$jars -s src -d bin src/cnt/{*,{d,g,t,u,m}*/*,*/*/*,n*/{B,O,G}*}.java 2>&1 | less
#javacSeven -cp .$jars -s src -d bin src/cnt/{*,{d,g,t,u,m}*/*,*/*/*,n*/{B,O,G}*}.java 2>&1 | less

#javacSeven -Xlint:all -cp .$jars -s src -d bin src/cnt/{*,*/*,*/*/*}.java 2>&1
javacSeven  -Xlint:all -cp .$jars -s src -d bin src/cnt/{*,{d,g,t,u,m}*/*,*/*/*,n*/{B,O,G}*}.java 2>&1
#javacSeven -cp .$jars -s src -d bin src/cnt/{*,{d,g,t,u,m}*/*,*/*/*,n*/{B,O,G}*}.java 2>&1
