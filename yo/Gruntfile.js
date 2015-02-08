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

    concat: {
      dist: {
        src: ['bower_components/jquery/dist/jquery.js',
          'bower_components/underscore/underscore.js',
          'bower_components/backbone/backbone.js',
          'bower_components/marionette/lib/backbone.marionette.js'],
          dest: '../resources/public/js/<%= pkg.name %>.js'
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
  grunt.loadNpmTasks('grunt-contrib-concat');

  grunt.registerTask('default', ['sass','watch']);
}
