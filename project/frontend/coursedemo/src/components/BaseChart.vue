<template>
<e-charts 
  :option="{
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'cross' }
  },
  xAxis: {
    type: 'category',
    data: labels
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      type: 'line',
      data: values,
      color: 'rgb(34, 126, 143)'
    }
  ],
  title: {
    text: title,
    left: 'center',
    textStyle: {
      fontWeight: 'normal'
    }
  }
}"/>
</template>
<script setup lang="ts">
defineProps({
  title: {type: String, default: ''},
  labels: {type: Array<String>, default: []},
  values: {type: Array<number>, default: []},
})
</script>