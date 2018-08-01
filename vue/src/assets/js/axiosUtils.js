import axios from 'axios'
import { Loading } from 'element-ui';
import store from '../../store';
import conf from './config';

let AxiosUtils=function(){
    var instance=axios.create({
        baseURL: conf.baseURL,
        timeout: 10000,
    });
    var loading;
    instance.interceptors.request.use(function (config) {
        // 显示
        loading=Loading.service({
            lock:true,
            text:'正在努力加载中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        });
        config.headers['authKey']=store.state.authKey;
        return config;
    },function (error) {
        console.log(error);
        return Promise.reject(error);
    });
    instance.interceptors.response.use(function (response) {
        // 隐藏
        if(loading){
            loading.close();
        }
        return response.data;
    },function (error) {
        // 隐藏
        if(loading){
            loading.close();
        }
        console.log(error);
        return Promise.reject(error);
    });
    /**
     *
     * @param url
     * @param obj
     * @param option=json/upload,默认是form提交
     * @returns {*}
     */
    let post= function (url,obj,option) {
        var headers={};
        var postData={};
        if(option==undefined||option=='form'){
            headers={'Content-Type': 'application/x-www-form-urlencoded'};
            postData=object2FormData(obj);
        }

        if(option=='upload'){
            headers={'Content-Type': 'multipart/form-data'};
            postData=object2FormData(obj);
        }else if(option=='json'){
            headers={'Content-Type': 'application/json'}
            postData=JSON.stringify(obj);
        }
        instance.headers=headers;
        return instance.post(url,postData);
    };
    /**
     * get方式请求
     * @param url
     */
    let get=function(url){
        return instance.get(url)
    };
    /**
     *
     * @param obj
     * @returns {*}
     */
    let object2FormData=function(obj){
        var form_data = new FormData();

        for ( var key in obj ) {
            form_data.append(key, obj[key]);
        }
        return form_data;
    };

    return {
        post,
        get
    }
};

// Vue.prototype.$axios=new AxiosUtils();
export default new AxiosUtils();
