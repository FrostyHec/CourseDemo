<template>
  <div/>
  <div v-html="renderedMarkdown" class="markdown"></div>
</template>

<script>
import MarkdownIt from 'markdown-it';
import mk from 'markdown-it-katex';
import hljs from 'highlight.js'

export default {
  props: {
    markdownContent: {
      type: String,
      default: `# Title
  
这是**一个**数学公式示例：$\\forall p,q\\in \\mathbb{N},\\ {p\\over q}\\ne\\sqrt 2$
\`\`\`ts
renderedMarkdown() {
  const md = new MarkdownIt();
  md.use(mk);
  return md.render(this.markdownContent);
}
\`\`\`

\`happy\`
`,
    }
  },
  computed: {
    renderedMarkdown() {
      const md = new MarkdownIt({
        html: true,
        highlight: function (code, lang) {
          if (lang && hljs.getLanguage(lang)) {
            return hljs.highlight(code, { language: lang }).value;
          }
          return hljs.highlightAuto(code).value;
        },
      });
      md.use(mk);
      return md.render(this.markdownContent);
    }
  }
};
</script>

<style>
/* 在这里添加KaTeX的CSS样式 */
@import "highlight.js/styles/atom-one-light.css";
@import 'katex/dist/katex.min.css';
.markdown pre {
  background-color: var(--ep-color-primary-light-9);
  padding: 12px;
  border-radius: 5px;
}
.markdown pre > code {
  padding: 0;
  border-radius: 0;
  color: var(--ep-color-primary-dark-2);
  background: none;
}
.markdown code {
  font-size: large;
}
</style>