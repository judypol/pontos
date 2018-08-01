var mutations={
    setAuthKey(state,value){
        var val=JSON.stringify(value.value);
        window.localStorage.setItem(value.name,val);
        state.authKey=value.value;
    },
    getAuthKey(state,name){
        var val=window.localStorage.getItem(name);
        if(val!=null){
            val=JSON.parse(val);
        }
        state.authKey=val;
    },
    setExecutors(state,val){
        state.executors=val;
    }
}
export default mutations;