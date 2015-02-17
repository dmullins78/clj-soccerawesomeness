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
          cwd: 'sass',
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
          'bower_components/marionette/lib/backbone.marionette.js',
          'js/Marionette.js',
          'dist/templates/all.js'],
          dest: '../resources/public/js/<%= pkg.name %>.js'
      }
    },

    jst: {
      compile: {
        files: {
          "dist/templates/all.js": ["templates/**/*.html"]
        }
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
        files: 'sass/**/*.scss',
        tasks: ['sass']
      }
    }
  });

  grunt.loadNpmTasks('grunt-sass');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-jst');

  grunt.registerTask('default', ['sass','watch']);
}
