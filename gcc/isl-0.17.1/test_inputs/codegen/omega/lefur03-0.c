for (int c0 = 0; c0 <= 3; c0 += 1)
  for (int c1 = max(2 * c0 - 3, c0 / 2); c1 <= min(3, c0 + 1); c1 += 1)
    for (int c2 = c0; c2 <= min(min(3, 2 * c0 - c1 + 1), 3 * c1 + 2); c2 += 1)
      for (int c3 = max(max(max(0, c1 - (-c1 + 3) / 3), 2 * c0 - (c0 + c1 + 1) / 3 - 1), c2 + floord(3 * c1 - c2 - 1, 6)); c3 <= min(3, c0 + 1); c3 += 1)
        for (int c4 = max(max(max(max(-200 * c1 + 400 * c3 - 199, 250 * c3 + 1), 667 * c0 - 333 * c1 - (c0 + c1 + 3) / 3 - 332), 333 * c1 + c1 / 3), 333 * c2 + (c2 + 1) / 3); c4 <= min(min(min(min(1000, 500 * c0 + 499), -200 * c1 + 400 * c3 + 400), 333 * c2 - (-c2 + 3) / 3 + 333), 333 * c3 - (-c3 + 3) / 3 + 334); c4 += 1)
          for (int c5 = max(max(max(c4, 1000 * c0 - c4), 1000 * c3 - 2 * c4 + 2), 500 * c1 + (c4 + 1) / 2); c5 <= min(min(min(2 * c4 + 1, 1000 * c0 - c4 + 999), 1000 * c3 - 2 * c4 + 1001), 500 * c1 + (c4 + 1) / 2 + 499); c5 += 1)
            s0(c0, c1, c2, c3, c4, c5);
