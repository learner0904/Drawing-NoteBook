<template>
  <div class="d3-runner-container">
    <h3>D3.js 绘图页面</h3>
    <textarea v-model="d3Code" rows="10" placeholder="输入 D3.js 代码"></textarea>
    <button @click="runD3Code">运行代码</button>
    <button @click="openInteractiveChart">查看互动图</button> <!-- 新增按钮 -->
    <button @click="saveD3Output">保存</button>
    <div ref="d3Output" class="d3-output"></div>

    <!-- 显示图片 -->
    <div v-if="pictureUrl" class="note-picture">
      <img :src="pictureUrl" alt="D3 Output" style="max-width: 100%; margin-top: 10px;">
    </div>
  </div>
</template>

<script>
import * as d3 from 'd3';
import axios from 'axios';
import * as Plot from '@observablehq/plot'; // 导入 Plot
import { h } from "vue";  // 确保在此处导入 h 函数
 
export default {
  name: "D3Runner",
  data() {
    return {
      d3Code: this.$route.query.code || "", // 存储用户输入的 D3.js 代码
      pictureUrl: this.$route.query.picture || "", // 存储图片URL
      noteId: this.$route.query.id || null, // 从路由获取 noteId，若无则为新建
    };
  },
  created() {
    if (this.noteId) {
      this.fetchNoteDetails();
    }
  },
  methods: {
    fetchNoteDetails() {
      // 将请求 URL 修改为路径变量形式
      this.$request.get(`/notes/selectById/${this.noteId}`)
        .then(res => {
          if (res.code === '200' && res.data) {
            this.d3Code = res.data.code || ""; // 将 code 放入 d3Code
            this.pictureUrl = res.data.picture || ""; // 将图片 URL 放入 pictureUrl
          } else {
            this.$message.error(res.msg || '无法加载Note详情');
          }
        })
        .catch(error => {
          console.error("获取 Note 详情失败:", error);
          this.$message.error('获取 Note 详情失败');
        });
    },
    runD3Code() {
      const svgContainer = this.$refs.d3Output;

      // 清空以前的图像
      while (svgContainer.firstChild) {
        svgContainer.removeChild(svgContainer.firstChild);
      }

      // 运行用户输入的代码
      try {
        if (this.d3Code.includes('Plot')) {
          if (this.d3Code.includes('data')) {
            function parseCSV(csvData, delimiter = ',') {
              const rows = csvData.trim().split('\n');
              const headers = rows[0].split(delimiter).map(header => header.trim());

              const data = rows.slice(1).map(row => {
                const values = row.split(delimiter).map(value => value.trim());
                const obj = {};

                headers.forEach((header, index) => {
                  obj[header] = isNaN(values[index]) ? values[index] : parseFloat(values[index]);
                });

                return obj;
              });

              return data;
            }

            const filenameMatch = this.d3Code.match(/data\s*=\s*"([^"]+)"/);
            const filename = filenameMatch ? filenameMatch[1] : null;
            const code = this.d3Code.split('\n').slice(1).join('\n').trim();
            console.log('filename:', filename); // 调试输出
            console.log('code:', code); // 调试输出
            // 确保在请求成功后再调用 userFunc

            const fileUrl = 'http://localhost:9090' + '/files/' + filename;
            console.log('fileUrl:', fileUrl); // 调试输出
            axios.get(fileUrl)
              .then(response => {
                console.log('CSV data:', response.data); // 调试输出
                const csvdata = parseCSV(response.data, ',');
                console.log('CSV data:', csvdata); // 调试输出
                console.log(`Generated function code: const plot = Plot.plot(${code});`);

                const userFunc = new Function('Plot', 'container', 'data', `
                  const plot = Plot.plot({${code}});
                  container.appendChild(plot);
                `);
                userFunc(Plot, svgContainer, csvdata); // 在这里调用 userFunc，确保 csvdata 已赋值
              })
              .catch(error => {
                console.error('Error fetching CSV data:', error); // 错误处理
              });

          } else {
            // 包装用户输入的代码
            const userFunc = new Function('Plot', 'container',
              `const plot = Plot.plot(${this.d3Code});
              container.appendChild(plot);`
            );
            userFunc(Plot, svgContainer);
          }

        } else {
          const userFunc = new Function('d3', 'container', this.d3Code);
          userFunc(d3, svgContainer);
        }
      } catch (error) {
        console.error("代码执行出错:", error);
        alert("代码执行出错，请检查代码。");
      }
    },

    saveD3Output() {
  const svgElement = this.$refs.d3Output.querySelector('svg');
  const canvas = document.createElement('canvas');
  const ctx = canvas.getContext('2d');
  
  // 检查是否有可保存的 SVG
  if (!svgElement) {
    // 如果没有 SVG，则绘制“不存在”的图像
    canvas.width = 200; // 可以根据需要调整大小
    canvas.height = 100;

    // 设置背景色和字体样式
    ctx.fillStyle = 'white';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    
    const lines = [
  '图形为动态图',
  '请在笔记内查看'
];

// 设置字体样式
ctx.fillStyle = 'black';
ctx.font = '20px Arial';
ctx.textAlign = 'center';
ctx.textBaseline = 'middle';

// 逐行绘制文本
lines.forEach((line, index) => {
  ctx.fillText(line, canvas.width / 2, canvas.height / 2 + (index * 25)); // 每行之间间隔25像素
});
  } else {
    // 将 SVG 转换为 PNG 图片
    const svgData = new XMLSerializer().serializeToString(svgElement);
    const svgBlob = new Blob([svgData], { type: 'image/svg+xml;charset=utf-8' });
    const url = URL.createObjectURL(svgBlob);
    const img = new Image();

    img.onload = () => {
      // 设置 canvas 大小与 SVG 一致
      canvas.width = img.width;
      canvas.height = img.height;

      // 将 SVG 绘制到 canvas 上
      ctx.drawImage(img, 0, 0);
    };

    img.src = url; // 触发图像加载
  }

  // 将 canvas 转换为 PNG
  canvas.toBlob((blob) => {
    const formData = new FormData();
    formData.append('file', blob, 'd3-output.png');

    // 上传图片到服务器
    axios.post(this.$baseUrl + '/files/upload', formData)
      .then(response => {
        const pictureUrl = response.data.data;
        console.log('Picture URL:', pictureUrl); // 调试输出
        // 保存或者更新 notes
        const noteData = {
          id: this.noteId,
          comment: 'D3.js 图形',
          code: this.d3Code,
          picture: pictureUrl
        };

        this.$request({
          method: this.noteId ? 'put' : 'post',
          url: this.noteId ? '/notes/update' : '/notes/add',
          data: noteData
        }).then(res => {
          if (res.code === '200') {
            this.$message.success(this.noteId ? 'Note 更新成功' : 'Note 创建成功');
          } else {
            this.$message.error(res.msg || '操作失败');
          }
        }).catch(error => {
          console.error(this.noteId ? '更新 Note 失败:' : '创建 Note 失败:', error);
          this.$message.error('操作失败，请稍后再试');
        });
      })
      .catch(error => {
        console.error("上传图片失败:", error);
      });
  });
}
,
    openInteractiveChart() {
  const newWindow = window.open("", "_blank");
  newWindow.document.write(this.d3Code); // 确保这一行有结束的反引号
  newWindow.document.close(); // 关闭文档流
}
  }
}
</script>

<style scoped>
textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  font-family: monospace;
  font-size: 14px;
}

.d3-runner-container {
  background-color: white; /* 设置背景颜色为白色 */
  padding: 20px; /* 可选：添加一些内边距 */
  border-radius: 8px; /* 可选：圆角 */
}

.d3-output {
  border: 1px solid #ddd;
  padding: 10px;
  margin-top: 10px;
  min-height: 300px;
}

button {
  margin-right: 10px;
}

.note-picture img {
  max-width: 100%;
  margin-top: 10px;
}
</style>
