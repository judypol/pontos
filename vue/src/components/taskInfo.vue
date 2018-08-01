<template>
    <el-dialog :value="model" :title="title" :visible.sync="dialogModel.visible" width="800px" 
        :close-on-click-modal="false" @open="onOpen" @close="onClose">
            <el-form size="medium" :model="dialogModel" :rules="rules" ref="taskForm">
                <el-form-item>
                    <el-col :span="12">
                        <el-form-item label="执行器：" label-width="120px" porp="jobGroup" required >
                           <el-select v-model="dialogModel.jobGroup" placeholder="请选择执行器">
                                <el-option v-for="item in executors" :key="item.id" :label="item.title" :value="item.id"> </el-option>
                           </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="任务名：" label-width="120px" prop="jobDesc">
                            <el-input v-model="dialogModel.jobDesc"></el-input>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <el-form-item>
                    <el-col :span="12">
                        <el-form-item label="路由策略：" label-width="120px">
                            <el-select v-model="dialogModel.executorRouteStrategy">
                                <el-option v-for="item in executorRoute" :key="item.value" :label="item.name" :value="item.value"> </el-option>
                           </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="Cron：" label-width="120px" prop="jobCron">
                            <el-input v-model="dialogModel.jobCron"></el-input>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <el-form-item>
                    <el-col :span="12">
                        <el-form-item label="运行模式：" label-width="120px">
                            <el-select v-model="dialogModel.glueType">
                                <el-option v-for="item in glueType" :key="item.value" :label="item.name" :value="item.value"> </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="jobHandler：" label-width="120px" prop="executorHandler">
                            <el-input v-model="dialogModel.executorHandler"></el-input>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <el-form-item>
                   <el-form-item label="任务参数：" label-width="120px" >
                       <el-input type="textarea" :rows="4" v-model="dialogModel.executorParam"></el-input>
                   </el-form-item>
                </el-form-item>
                <el-form-item>
                    <el-col :span="12">
                        <el-form-item label="子任务ID：" label-width="120px">
                            <el-input placeholder="多个子任务用逗号隔开" v-model="dialogModel.childJobId"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="节假日：" label-width="120px">
                            <el-checkbox v-model="dialogModel.history"></el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <el-form-item>
                    <el-col :span="12">
                        <el-form-item label="超时时间：" label-width="120px">
                            <el-input v-model="dialogModel.executorTimeout"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="阻塞处理策略：" label-width="120px">
                            <el-select v-model="dialogModel.executorBlockStrategy">
                                <el-option v-for="item in executorBlock" :key="item.value" :label="item.name" :value="item.value"> </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <el-form-item>
                    <el-col :span="12">
                        <el-form-item label="失败处理策略：" label-width="120px">
                            <el-select v-model="dialogModel.executorFailStrategy">
                                <el-option v-for="item in executorFail" :key="item.value" :label="item.name" :value="item.value"> </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="报警邮件：" label-width="120px">
                            <el-input v-model="dialogModel.alarmEmail" placeholder="多个邮件用;分割"></el-input>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <el-form-item>
                    <el-col :span="12">
                        <el-form-item label="负责人：" label-width="120px">
                            <el-input v-model="dialogModel.author"></el-input>
                        </el-form-item>
                    </el-col>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogModel.visible = false">取 消</el-button>
                <el-button type="primary" @click="onSave">确 定</el-button>
            </span>
        </el-dialog>
</template>
<script>
export default {
    data(){
        return{
            executors:[],
            executorRoute:this.$store.state.executorRoute,
            glueType:this.$store.state.glueType,
            executorBlock:this.$store.state.executorBlock,
            executorFail:this.$store.state.executorFail,
            rules:{
                jobDesc: [
                    { required: true, message: '请输入任务名称', trigger: 'blur' },
                ],
                jobGroup: [
                    { type:Number,required: true, message: '请选择执行器', trigger: 'change' },
                ],
                jobCron:[
                    { required: true, message: '请输入Cron', trigger: 'blur' },
                ],
                executorHandler:[
                    { required: true, message: '请输入JobHandler', trigger: 'blur' },
                ],
            },
            dialogModel:{
                // id:null,
                // jobGroup:'',
                // jobCron:'',
                // jobDesc:'',
                // author:'',
                // alarmEmail:'',
                // executorRouteStrategy:'',
                // executorHandler:'',
                // executorParam:'',
                // executorBlockStrategy:'',
                // executorFailStrategy:'',
                // executorTimeout:60,
                // glueType:'',
                // childJobId:'',
                // holiday:'',
                // visible:false,
            },
        }
    },
    methods:{
        onOpen(){
            this.executors=this.$store.state.executors;
        },
        onClose(){
            console.log("onClose");
            this.$refs['taskForm'].resetFields();
            //done();
        },
        onSave(){
            if(!this.dialogModel.jobGroup){
                    this.$message('执行器不能为空');
                    return false;
                }
            this.$refs['taskForm'].validate((valid) => {
                
                if (valid) {
                    this.$emit('refresh');
                    this.dialogModel.visible=false;
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        }
    },
    props:['model','title'],
    // computed:{
    //   excutors:function(){
    //       return this.$store.state.excutors;
    //   }  
    // },
    watch:{
      model:{
            handler(newValue,oldValue){
                this.dialogModel = newValue;
            },
            deep:true
      },
      dialogModel:{
          handler(newValue,oldValue){
              this.$emit('input',newValue);
          },
          deep:true
      }
  }
}
</script>
