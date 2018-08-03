import axios from '../assets/js/axiosUtils'
var actions={
    setExecutors(context){
        if(context.state.executors.length==0){
            axios.post('/jobinfo/groupList',{filter:''})
            .then((res)=>{
                if(res.code==200){
                    context.commit('setExecutors',res.content);
                }
            });
        }
    },
}
export default actions;