#include <cstdarg>
#include <cstdint>
#include <cstdlib>
#include <ostream>
#include <new>

extern "C" {

void test();

bool start(const char *name, const char *ws, const char *listen);

bool stop(const char *name, const char *ws, const char *listen);

} // extern "C"
