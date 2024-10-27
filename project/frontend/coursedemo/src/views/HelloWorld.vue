<template>
  <div v-html="renderedMarkdown"></div>
</template>

<script>
import MarkdownIt from 'markdown-it';
import mk from 'markdown-it-katex';

export default {
  data() {
    return {
      markdownContent: `这是**一个**数学公式示例：$\\forall p,q\\in \\mathbb{N},\\ {p\\over q}\\ne\\sqrt 2$`
    };
  },
  computed: {
    renderedMarkdown() {
      const md = new MarkdownIt();
      md.use(mk);
      return md.render(this.markdownContent);
    }
  }
};
</script>

<style>
/* 在这里添加KaTeX的CSS样式 */
@import 'katex/dist/katex.min.css';
</style>