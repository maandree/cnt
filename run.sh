if [[ $# = 0 ]]; then
    java7 -ea -cp bin cnt.Program

elif [[ $1 = "main" ]]; then
    java7 -ea -cp bin cnt.Program

elif [[ $1 = "main-da" ]]; then
    java7 -da -cp bin cnt.Program

elif [[ $1 = "engine" ]]; then
    java7 -ea -cp bin cnt.demo.EngineDemo

elif [[ $1 = "frame" ]]; then
    java7 -ea -cp bin cnt.demo.MainFrameDemo

elif [[ $1 = "network" ]]; then
    java7 -ea -cp bin cnt.demo.NetworkingDemo

elif [[ $1 = "upnp" ]]; then
    java7 -ea -cp bin cnt.demo.UPnPDemo

elif [[ $1 = "shape" ]]; then
    java7 -ea -cp bin cnt.test.ShapeTest

elif [[ $1 = "linkedlist" ]]; then
    java7 -ea -cp bin cnt.test.CDLinkedListTest
    java7 -ea -cp bin cnt.test.ACDLinkedListTest

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
