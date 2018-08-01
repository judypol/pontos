import axios from '../assets/js/axiosUtils'
var actions={
    setExcutors(context){
        if(context.state.excutors.length==0){
            axios.post('/jobinfo/groupList',{filter:''})
            .then((res)=>{
                if(res.code==200){
                    context.commit('setExcutors',res.content);
                }
            });
        }
    }
}
export default actions;