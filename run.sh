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

elif [[ $1 = "--completion--" ]]; then
    _run()
    {
	local cur prev words cword
	_init_completion -n = || return
	
	COMPREPLY=( $( compgen -W 'main main-da' -- "$cur" ) )
    }
    
    complete -o default -F _run run
    
else
    echo "run: Rule missing.  Stop." >&2
fi
