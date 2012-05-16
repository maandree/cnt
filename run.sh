jars=":lib/cling-core-1.0.5.jar:lib/cling-support-1.0.5.jar:lib/teleal-common-1.0.13.jar"


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
    javaSeven -ea -cp bin$jars cnt.demo.EngineDemo

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

elif [[ $1 = "linkedlist" ]]; then
    javaSeven -ea -cp bin$jars cnt.test.CDLinkedListTest
    javaSeven -ea -cp bin$jars cnt.test.ACDLinkedListTest

elif [[ $1 = "--completion--" ]]; then
    _run()
    {
	local cur prev words cword
	_init_completion -n = || return
	
	COMPREPLY=( $( compgen -W 'main main-da engine frame network peernetwork chat upnp shape linkedlist' -- "$cur" ) )
    }
    
    complete -o default -F _run run
    
else
    echo "run: Rule missing.  Stop." >&2
fi
