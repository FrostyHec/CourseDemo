<!-- copyright vue-resizer -->
<template>
  <div
    class="drager_col"
    ref="container"
    :style="{ width: width, height: height }"
  >
    <div class="drager_left" :style="{ width: left + '%' }">
      <div>
        <slot name="left"></slot>
      </div>
    </div>
    <div
      class="slider_col"
      @touchstart.passive="mobileDragCol"
      @mousedown="dragCol"
      :style="{
        width: sliderWidth + 'px',
        marginLeft: -(sliderWidth+1) / 2 + 'px',
        marginRight: -(sliderWidth-1) / 2 + 'px',
      }"
    ></div>
    <div class="drager_right" :style="{ width: 100 - left + '%' }">
      <div>
        <slot name="right"></slot>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "DragCol",
  props: {
    leftPercent: {
      type: Number,
      default: 50,
    },
    sliderWidth: {
      type: Number,
      default: 20,
    },
    width: {
      type: String,
      default: "400px",
    },
    height: {
      type: String,
      default: "400px",
    },
    sliderColor: {
      type: String,
      default: "#6f808d",
    },
    sliderBgColor: {
      type: String,
      default: "#1f2e3a",
    },
    sliderHoverColor: {
      type: String,
      default: "#6f808d",
    },
    sliderBgHoverColor: {
      type: String,
      default: "#16222a",
    },
  },
  data() {
    return {
      left: this.leftPercent,
      isDragging: false,
    };
  },
  methods: {
    mobileDragCol(e) {
      e = e || window.event;
      e.stopPropagation();
      let oldPos = e.changedTouches[0].clientX;
      let oldPosPercent = this.left;
      let newPos = 0;
      let newPosPercent = 0;
      const containerWidth = this.$refs.container.offsetWidth;
      const vue = this;
      this.isDragging = true;
      this.$emit("isDragging", this.isDragging);
      document.ontouchmove = sliderDrag;
      document.ontouchend = cancelSliderDrag;
      function sliderDrag(e) {
        if (this.time && Date.now() - this.time < 40) return;
        this.time = Date.now();
        e = e || window.event;
        e.stopPropagation();
        newPos = e.changedTouches[0].clientX;
        const movingDistancePercent = parseFloat(
          (((oldPos - newPos) / containerWidth) * 100).toFixed(3)
        );
        newPosPercent = oldPosPercent - movingDistancePercent;
        if (newPosPercent <= 0) {
          vue.left = 0;
        } else if (newPosPercent >= 100) {
          vue.left = 100;
        } else {
          vue.left = newPosPercent;
        }
        vue.$emit("dragging", vue.left);
      }
      function cancelSliderDrag() {
        vue.isDragging = false;
        vue.$emit("isDragging", vue.isDragging);
        document.ontouchmove = null;
        document.ontouchend = null;
      }
    },
    dragCol(e) {
      e = e || window.event;
      e.preventDefault();
      e.stopPropagation();
      let oldPos = e.clientX;
      let oldPosPercent = this.left;
      let newPos = 0;
      let newPosPercent = 0;
      const containerWidth = this.$refs.container.offsetWidth;
      const vue = this;
      this.isDragging = true;
      this.$emit("isDragging", this.isDragging);
      document.onmousemove = sliderDrag;
      document.onmouseup = cancelSliderDrag;
      function sliderDrag(e) {
        if (this.time && Date.now() - this.time < 40) return;
        this.time = Date.now();
        e = e || window.event;
        e.preventDefault();
        e.stopPropagation();
        newPos = e.clientX;
        const movingDistancePercent = parseFloat(
          (((oldPos - newPos) / containerWidth) * 100).toFixed(3)
        );
        newPosPercent = oldPosPercent - movingDistancePercent;
        if (newPosPercent <= 0) {
          vue.left = 0;
        } else if (newPosPercent >= 100) {
          vue.left = 100;
        } else {
          vue.left = newPosPercent;
        }
        vue.$emit("dragging", vue.left);
      }
      function cancelSliderDrag() {
        vue.isDragging = false;
        vue.$emit("isDragging", vue.isDragging);
        document.onmouseup = null;
        document.onmousemove = null;
      }
    },
  },
};
</script>
<style>
.drager_col {
  overflow: hidden;
  display: flex;
  box-sizing: border-box;
}
.drager_col * {
  box-sizing: border-box;
}
.drager_col > div {
  height: 100%;
}
.drager_left > div {
  height: 100%;
  overflow: hidden;
}
.drager_right > div {
  height: 100%;
  overflow: hidden;
}
.drager_col > .slider_col {
  position: relative;
  z-index: 1;
  cursor: col-resize;
}
.drager_col > .slider_col:hover {
  background-color: var(--ep-border-color);
}
.drager_col > .slider_col:active {
  background-color: var(--ep-color-primary);
}
</style>