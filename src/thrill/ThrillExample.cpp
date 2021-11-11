#include <thrill/api/read_lines.hpp>
#include <thrill/api/write_lines.hpp>
#include <thrill/api/reduce_by_key.hpp>
#include <tlx/string/split_view.hpp>
#include <chrono>

using namespace thrill;
using Pair = std::pair<std::string, size_t>;

void WordCountExample(Context &ctx) {
    auto lines = ReadLines(ctx, "../data/wordcount");
    auto word_pairs = lines.FlatMap<Pair>([](const std::string &line, auto emit) {
        tlx::split_view(' ', line, [&](tlx::string_view sv) {
            emit(Pair(sv.to_string(), 1));
        });
    });
    word_pairs.ReduceByKey([](const Pair &p) {
        return p.first;
    }, [](const Pair &a, const Pair &b) {
        return Pair(a.first, a.second + b.second);
    }).Map([](const Pair &p) {
        return p.first + ": " + std::to_string(p.second);
    }).WriteLines("../data/thrill/wordcount-#");
}

int main(int argc, char *argv[]) {
    putenv("THRILL_LOCAL=1");
    putenv("THRILL_WORKERS_PER_HOST=4");
    auto start = std::chrono::steady_clock::now();
    Run(WordCountExample);
    auto end = std::chrono::steady_clock::now();
    auto elapsed = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "Job took " << elapsed << " ms" << std::endl;
    return EXIT_SUCCESS;
}
