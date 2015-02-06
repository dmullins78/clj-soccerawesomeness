module.exports = function(grunt) {
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    sass: {
      options: {
        includePaths: ['bower_components/foundation/scss']
      },
      dist: {
        options: {
          outputStyle: 'expanded'
        },

        files: [{
          expand: true,
          cwd: '../resources/sass',
          src: ['*.scss'],
          dest: '../resources/public/css',
          ext: '.css'
        }]
      }
    },

    watch: {
      grunt: {
        files: ['Gruntfile.js'],
        options: {
          livereload: true,
        }
      },

      sass: {
        files: '../resources/sass/**/*.scss',
        tasks: ['sass']
      }
    }
  });

  grunt.loadNpmTasks('grunt-sass');
  grunt.loadNpmTasks('grunt-contrib-watch');

  grunt.registerTask('default', ['sass','watch']);
}
