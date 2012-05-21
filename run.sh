jars=":lib/cling-core-1.0.5.jar:lib/cling-support-1.0.5.jar:lib/teleal-common-1.0.13.jar:lib/libandree.jar:lib/jmenumaker.jar"


[[ $(echo `java -version 2>&1 | cut -d . -f 2` | cut -d ' ' -f 1) = '7' ]] &&
    function javaSeven()
    {
	java $@
    }

[[ $(echo `java -version 2>&1 | cut -d . -f 2` | cut -d ' ' -f 1) = '7' ]] ||
    function javaSeven() {
	java7 $@
    }


if [[ $# = 0 ]]; then
    javaSeven -ea -cp bin$jars cnt.Program

elif [[ $1 = "main" ]]; then
    javaSeven -ea -cp bin$jars cnt.Program

elif [[ $1 = "main-da" ]]; then
    javaSeven -da -cp bin$jars cnt.Program

elif [[ $1 = "engine" ]]; then
    javaSeven -ea -cp bin$jars cnt.demo.EngineDemo $2

elif [[ $1 = "frame" ]]; then
    javaSeven -ea -cp bin$jars cnt.demo.MainFrameDemo

elif [[ $1 = "network" ]]; then
    javaSeven -ea -cp bin$jars cnt.demo.NetworkingDemo

elif [[ $1 = "peernetwork" ]]; then
    javaSeven -ea -cp bin$jars cnt.demo.PeerNetworkingDemo $2 $3 $4 $5 $6

elif [[ $1 = "chat" ]]; then
    javaSeven -ea -cp bin$jars cnt.demo.ChatDemo $2 $3 $4 $5 $6

elif [[ $1 = "upnp" ]]; then
    javaSeven -ea -cp bin$jars cnt.demo.UPnPDemo

elif [[ $1 = "shape" ]]; then
    javaSeven -ea -cp bin$jars cnt.test.ShapeTest

elif [[ $1 = "pipe" ]]; then
    javaSeven -ea -cp bin$jars cnt.test.PipeTest

elif [[ $1 = "linkedlist" ]]; then
    javaSeven -ea -cp bin$jars cnt.test.CDLinkedListTest
    javaSeven -ea -cp bin$jars cnt.test.ACDLinkedListTest

elif [[ $1 = "game" ]]; then
    stty -icanon
    javaSeven -ea -cp bin$jars cnt.demo.GameDemo
    stty icanon

elif [[ $1 = "replay" ]]; then
    javaSeven -ea -cp bin$jars cnt.Replayer /dev/shm/recording.cnt

elif [[ $1 = "peergame" ]]; then
    stty -icanon
    javaSeven -ea -cp bin$jars cnt.demo.PeerGameDemo $2 $3 $4 $5 $6
    stty icanon

elif [[ $1 = "connection" ]]; then
    javaSeven -ea -cp bin$jars cnt.demo.ConnectionDemo $2 $3 $4 $5 $6

elif [[ $1 = "--completion--" ]]; then
    _run()
    {
	local cur prev words cword
	_init_completion -n = || return
	
	if [[ "$prev" = "engine" ]]; then
	    COMPREPLY=( $( compgen -W 'clockwise anti-clockwise' -- "$cur" ) )
	else
	    COMPREPLY=( $( compgen -W 'main main-da engine frame network peernetwork chat' -- "$cur" ) \
		        $( compgen -W 'upnp shape pipe linkedlist game replay peergame connection' -- "$cur" ))
	fi
    }
    
    complete -o default -F _run run
    
else
    echo "run: Rule missing.  Stop." >&2
fi
