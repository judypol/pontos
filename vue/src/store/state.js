var state={
    authKey:null,
    executors:[],        //执行器
    logStatus:[
        {value:-1,name:'全部'},{value:1,name:'成功'},{value:2,name:'失败'},{value:3,name:'运行中'},
    ],
    jobHandlers:[],      //jobHandlers
    executorRoute:[      //执行路径策略
       { value:'FIRST',name:'第一个'},{ value:'LAST',name:'最后一个'},{ value:'ROUND',name:'轮询'},{ value:'RANDOM',name:'随机'},
       { value:'CONSISTENT_HASH',name:'一致性HASH'},{ value:'LEAST_FREQUENTLY_USED',name:'最不常用'},{ value:'LEAST_RECENTLY_USED',name:'最近最久未用'},{ value:'FAILOVER',name:'故障转移'},
       { value:'BUSYOVER',name:'忙碌转移'},{ value:'SHARDING_BROADCAST',name:'分片广播'}
    ],
    glueType:[          //执行方式
        {value:'BEAN',name:'BEAN'},{value:'GLUE_GROOVY',name:'GLUE(JAVA)'},{value:'GLUE_SHELL',name:'GLUE(Shell)'},
        {value:'GLUE_PYTHON',name:'GLUE(Python)'},{value:'GLUE_NODEJS',name:'GLUE(Nodejs)'}
    ],
    executorBlock:[      //执行阻塞策略
        {value:'SERIAL_EXECUTION',name:'单机串行'},{value:'DISCARD_LATER',name:'丢弃后续调度'},
        {value:'COVER_EARLY',name:'覆盖之前调度'}
    ],
    executorFail:[       //执行失败策略
        {value:'NULL',name:'无'},{value:'FAIL_TRIGGER_RETRY',name:'调度失败重试'},
        {value:'FAIL_HANDLE_RETRY',name:'执行失败重试'}
    ]
}

export default state;