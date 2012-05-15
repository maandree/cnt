jars=":lib/cling-core-1.0.5.jar:lib/cling-support-1.0.5.jar:lib/teleal-common-1.0.13.jar"

if [[ $# = 0 ]]; then
    java7 -ea -cp bin$jar cnt.Program

elif [[ $1 = "main" ]]; then
    java7 -ea -cp bin$jar cnt.Program

elif [[ $1 = "main-da" ]]; then
    java7 -da -cp bin$jar cnt.Program

elif [[ $1 = "engine" ]]; then
    java7 -ea -cp bin$jar cnt.demo.EngineDemo

elif [[ $1 = "frame" ]]; then
    java7 -ea -cp bin$jar cnt.demo.MainFrameDemo

elif [[ $1 = "network" ]]; then
    java7 -ea -cp bin$jar cnt.demo.NetworkingDemo

elif [[ $1 = "upnp" ]]; then
    java7 -ea -cp bin$jar cnt.demo.UPnPDemo

elif [[ $1 = "shape" ]]; then
    java7 -ea -cp bin$jar cnt.test.ShapeTest

elif [[ $1 = "linkedlist" ]]; then
    java7 -ea -cp bin$jar cnt.test.CDLinkedListTest
    java7 -ea -cp bin$jar cnt.test.ACDLinkedListTest

elif [[ $1 = "--completion--" ]]; then
    _run()
    {
	local cur prev words cword
	_init_completion -n = || return
	
	COMPREPLY=( $( compgen -W 'main main-da engine frame network upnp shape linkedlist' -- "$cur" ) )
    }
    
    complete -o default -F _run run
    
else
    echo "run: Rule missing.  Stop." >&2
fi
