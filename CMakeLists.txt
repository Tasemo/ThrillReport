cmake_minimum_required(VERSION 3.0.0)
project(ThrillReport VERSION 1.0)

add_subdirectory(lib/thrill)
add_executable(ThrillReport src/thrill/ThrillExample.cpp)
target_link_libraries(ThrillReport PUBLIC thrill)
target_include_directories(ThrillReport PUBLIC "${PROJECT_SOURCE_DIR}/lib/thrill")
