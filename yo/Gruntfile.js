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
          'js/libs/backbone.autocomplete-min.js',
          'bower_components/marionette/lib/backbone.marionette.js',
          'js/Marionette.js'],
          dest: '../resources/public/js/<%= pkg.name %>.js'
      },

      admin: {
        src: ['bower_components/jquery/dist/jquery.js', 'js/modules/admins/Init.js'],
        dest: '../resources/public/js/admin.js'
      },

      playerStats: {
        src: ['dist/templates/all.js', 'js/modules/players/models/*.js', 'js/modules/players/views/*.js', 'js/modules/players/Init.js'],
        dest: '../resources/public/js/player-stats.js'
      },

      rosters: {
        src: ['dist/templates/all.js', 'js/modules/aliases/models/*.js', 'js/modules/aliases/views/*.js', 'js/modules/aliases/Init.js'],
        dest: '../resources/public/js/rosters.js'
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
      options: {
        livereload: true,
      },

      grunt: {
        files: ['Gruntfile.js'],
      },

      templates: {
        files: 'templates/**/*.html',
        tasks: ['jst','concat']
      },

      js: {
        files: 'js/**/*.js',
        tasks: ['concat']
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

  grunt.registerTask('default', ['sass','jst','concat']);
}
