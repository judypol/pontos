<template>
    <div>
        <div class="top">
            <el-button type="primary" @click.prevent="addDialog">新增执行器</el-button>
        </div>
        <div>
            <el-table :border="true" :data="tbModel">
                <el-table-column label="排序" prop="order"></el-table-column>
                <el-table-column label="APPName" prop="appName"></el-table-column>
                <el-table-column label="名称" prop="title"></el-table-column>
                <el-table-column label="注册方式">
                    <template slot-scope="scope">
                        <template v-if="scope.row.addressType===0">自动注册</template>
                        <template v-else>手动注册</template>
                    </template>
                </el-table-column>
                <el-table-column label="Online机器地址">
                    <template slot-scope="scope">
                        <el-tag>{{scope.row.addressList}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="primary" size="small" @click.prevent="editDialog(scope.row)">编辑</el-button>
                        <el-button size="small" @click.prevent="remove(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">   
            <el-form :model="dialogModel" label-width="100px" :rules="rules" ref="executorFrm">
                <el-form-item label="AppName" prop="appName">
                    <el-input v-model="dialogModel.appName" placeholder="必须与执行器里的名字一致"></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="title">
                    <el-input v-model="dialogModel.title" placeholder="名称"></el-input>
                </el-form-item>
                <el-form-item label="排序" prop="order">
                    <el-input v-model="dialogModel.order" placeholder="排序"></el-input>
                </el-form-item>
                <el-form-item label="注册方式" prop="addressType">
                    <el-radio v-model="dialogModel.addressType" :label="0">自动注册</el-radio>
                    <el-radio v-model="dialogModel.addressType" :label="1">手动注册</el-radio>
                </el-form-item>
                <el-form-item label="机器地址">
                    <el-input v-model="dialogModel.addressList" type="textarea" :rows="3" :disabled="dialogModel.addressType===0" placeholder="机器地址"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleOk">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<style>
    .el-table td, .el-table th{
        padding:5px 0;
    }
</style>
<script>
import ExecutorModel from '../../models/executorModel';
export default {
    data(){
        return{
            dialogTitle:'新增执行器',
            dialogVisible:false,
            dialogModel:new ExecutorModel(),
            tbModel:[],
            rules:{
                appName:[
                    {required:true,message:'AppName不能为空',trigger:'blur'}
                ],
                title:[
                    {required:true,message:'名称不能为空',trigger:'blur'}
                ],
                order:[
                    {required:true,message:'排序不能为空',trigger:'blur'}
                ],
                addressType:[
                    {required:true,message:'注册方式不能为空',trigger:'change'}
                ],
            },
            
        }
    },
    methods:{
        addDialog(){
            this.dialogModel=new ExecutorModel();
            this.dialogTitle="新增执行器";
            this.dialogVisible=true;
        },
        editDialog(row){
            this.dialogModel=row;
            this.dialogTitle="编辑执行器";
            this.dialogVisible=true;
        },
        onSearch(){
            this.$axios.post('/jobgroup/groupList',{}).then((res)=>{
                if(res.code===200){
                    this.tbModel=res.content;
                }else{
                    this.$message.error(res.msg);
                }
            })
        },
        handleOk(){
            var alertTitle="编辑成功！";
            var url='/jobgroup/update';
            if(this.dialogModel.id===0){
                url='/jobgroup/save';
                alertTitle="新增成功！";
            }

            this.$refs['executorFrm'].validate((valiate)=>{
                if(valiate){
                    this.$axios.post(url,this.dialogModel).then((res)=>{
                        if(res.code===200){
                            this.$alert(alertTitle,'提示').then(()=>{
                                this.onSearch();
                                this.dialogVisible=false;
                            });
                        }else{
                            this.$message.error(res.msg);
                        }
                        this.$refs['executorFrm'].resetFields();
                    })
                }
            });
        },
        remove(row){
            this.$confirm('确定删除吗？').then(()=>{
                this.$axios.post('/jobgroup/remove',{id:row.id}).then((res)=>{
                    if(res.code===200){
                        this.$alert('删除成功','提示').then(()=>{
                            this.onSearch();
                        });
                    }else{
                        this.$message.error(res.msg);
                    }
                });
            }).catch(()=>{

            });
        }
    },
    created(){
        this.onSearch();
        
    },
    computed:{
        // typeDisabled:()=>{
        //     return this.dialogModel.addressType===0;
        // }
    }
}
</script>
