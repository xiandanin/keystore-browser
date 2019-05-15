<template>
    <div id="app" v-loading.fullscreen.lock="loading">
        <div v-show="keystore==null" class="upload-container">
            <el-upload
                    drag
                    :show-file-list="false"
                    accept=".apk,.jks,.keysotre"
                    :before-upload="handleBeforeUpload"
                    action="#">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将<em> Keystore 、 Apk </em>拖到此处</div>
            </el-upload>
        </div>
        <div v-show="keystore!=null">
            <div style="text-align: right;margin-bottom: 1%">
                <el-button size="mini" @click="handleClickReset">重新上传</el-button>
            </div>
            <el-tabs type="card" v-model="active" v-if="keystore != null">
                <el-tab-pane v-for="it in keystore.aliases" :label="it.alias" :name="it.alias">
                    <!--apk基本信息-->
                    <div v-if="isApk">
                        <div>
                            <span class="label">包名：</span>
                            <span class="value">{{it.packageName}}</span>
                        </div>
                        <div>
                            <span class="label">版本代码：</span>
                            <span class="value">{{it.versionCode}}</span>
                        </div>
                        <div>
                            <span class="label">版本名称：</span>
                            <span class="value">{{it.versionName}}</span>
                        </div>
                        <div>
                            <span class="label">签名类型：</span>
                            <span class="value">{{it.signatureType}}</span>
                        </div>
                        <div class="node-container" v-if="it.extras.length>0">
                            <span class="label">额外信息：</span>
                            <div v-for="(value,key) in it.extras" class="node-content">
                                <span class="label">{{key}}：</span>
                                <span class="value">{{value}}</span>
                            </div>
                        </div>
                    </div>

                    <!--keystore基本信息-->
                    <div v-else>
                        <div><span class="label">别名：</span><span class="value">{{it.alias}}</span></div>

                        <div><span class="label">创建时间：</span><span class="value">{{it.created_time_str}}</span></div>
                    </div>
                    <div class="node-container">
                        <div class="label">所有者信息</div>
                        <div class="node-content">
                            <div>
                                <span class="label">通用名称：</span>
                                <span class="value">{{it.cert.subject.commonName}}</span>
                            </div>
                            <div>
                                <span class="label">组织名称：</span>
                                <span class="value">{{it.cert.subject.organizationName}}</span>
                            </div>
                            <div>
                                <span class="label">组织单位：</span>
                                <span class="value">{{it.cert.subject.organizationUnit}}</span>
                            </div>
                            <div>
                                <span class="label">地区名称：</span>
                                <span class="value">{{it.cert.subject.localityName}}</span>
                            </div>
                            <div>
                                <span class="label">省份名称：</span>
                                <span class="value">{{it.cert.subject.stateName}}</span>
                            </div>
                            <div>
                                <span class="label">国家代码：</span>
                                <span class="value">{{it.cert.subject.country}}</span>
                            </div>
                        </div>
                    </div>
                    <!--证书信息-->
                    <div class="node-container">
                        <div class="label">证书信息</div>
                        <div class="node-content">
                            <div>
                                <span class="label">版本：</span>
                                <span class="value">{{it.cert.version}}</span>
                            </div>
                            <div>
                                <span class="label">有效期：</span>
                                <span class="value">{{it.cert.start_time_str}} 至 {{it.cert.end_time_str}}</span>
                            </div>
                            <div>
                                <span class="label">序列号：</span>
                                <span class="value">{{it.cert.serialNumber}}</span>
                            </div>
                            <div>
                                <span class="label">签名算法名称：</span>
                                <span class="value">{{it.cert.signature_algorithm}}</span>
                            </div>
                            <div>
                                <span class="label">主体公共密钥算法：</span>
                                <span class="value">{{it.cert.publicKeyAlgorithm}}</span>
                            </div>

                            <div>
                                <span class="label">MD5：</span>
                                <span class="value" @click="doCopy">{{it.cert.signature.md5}}</span>
                            </div>
                            <div>
                                <span class="label">SHA1：</span>
                                <span class="value" @click="doCopy">{{it.cert.signature.sha1}}</span>
                            </div>
                            <div>
                                <span class="label">SHA256：</span>
                                <span class="value" @click="doCopy">{{it.cert.signature.sha256}}</span>
                            </div>
                        </div>
                    </div>

                    <!--apk平台格式-->
                    <div v-if="isApk" class="node-container">
                        <div class="label">开放平台格式</div>
                        <div class="node-content">
                            <div v-for="f in it.cert.signature.format">
                                <span class="label" @click="openUrl(f.link)">{{f.displayName}}：</span>
                                <span class="value" @click="doCopy">{{f.format}}</span>
                            </div>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
  import path from 'path'
  import {shell} from 'electron'

  const jarPath = path.join(__static, '/keystore-java-library.jar')

  const exec = require('child_process').exec
  export default {
    data () {
      return {
        loading: false,
        active: '',
        params: {
          storepass: null,
          filepath: null
        },
        keystore: null,
        isApk: false
      }
    },
    created () {

    },
    methods: {
      loadFileInfo () {
        // 如果不是apk 又有密码
        if (!this.isApk && !(this.params.storepass != null && this.params.storepass.length > 0)) {
          return
        }
        this.loading = true

        let that = this
        // let cmdStr = 'java -jar /Users/dengyuhan/workspace/Github-Public/keystore-browser/keystore-java-library/out/keystore-java-library.jar /Users/dengyuhan/Downloads/DigizenAppStore.jks '+password
        let cmdStr = 'java -jar ' + jarPath + ' ' + this.params.filepath + ' ' + this.params.storepass
        console.log(cmdStr)
        let workerProcess = exec(cmdStr)
        // 打印错误的后台可执行程序输出
        workerProcess.stdout.on('data', function (data) {
          let result = JSON.parse(data)
          if (result.success) {
            // 如果是apk 就转成页面格式
            let obj = result.data
            that.isApk = obj.packageName != null && obj.packageName.length > 0
            if (that.isApk) {
              let aliases = []
              aliases.push(obj)
              that.keystore = {aliases: aliases}
            } else {
              // 如果是keystore 就直接赋值
              that.keystore = obj
            }
            that.active = that.keystore.aliases[0].alias
            console.log(JSON.stringify(that.keystore))
          } else {
            that.errorMessage(result.message)
          }
        })

        // 退出之后的输出
        workerProcess.on('close', function (code) {
          that.loading = false
        })
      },
      doCopy (event) {
        let that = this
        this.$copyText(event.target.innerText).then(function (e) {
          that.$message({
            message: '复制成功',
            type: 'success'
          })
        }, function (e) {

        })
      },
      errorMessage (message) {
        this.$message({
          message: message,
          type: 'error'
        })
      },
      openUrl (url) {
        shell.openExternal(url)
      },
      openPasswordInput () {
        this.$prompt('请输入Keystore密码', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /\S/,
          inputErrorMessage: '请输入正确的密码'
        }).then(({value}) => {
          this.params.storepass = value
          this.loadFileInfo()
        }).catch(() => {
        })
      },
      handleBeforeUpload (file) {
        this.params.filepath = file.path
        // 是否apk
        console.log(this.params.filepath)
        let suffix = '.apk'
        this.isApk = this.params.filepath.lastIndexOf(suffix) === this.params.filepath.length - suffix.length
        if (!this.isApk) {
          this.openPasswordInput()
        } else {
          this.loadFileInfo()
        }
        return false
      },
      handleClickReset () {
        this.keystore = null
        this.params = {}
      }
    }
  }
</script>

<style>
    /* CSS */
    #app {
        padding: 2%;
    }

    .label {
        color: #333;
    }

    .value {
        color: #777;
    }

    .node-container {
        margin-top: 2%;
    }

    .node-content {
        margin-left: 32px;
    }

    .upload-container {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
    }
</style>
